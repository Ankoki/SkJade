package com.ankoki.skjade.elements.lasers.section;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.config.validate.SectionValidator;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.elements.lasers.Laser;
import com.ankoki.skjade.elements.lasers.LaserManager;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.List;

@Name("Create Laser Section")
@Description({"Creates a laser based on the giving entries.",
		"The usable entries are: ",
		" • duration (required)",
		" • start location (required) ",
		" • type",
		" • end location",
		" • end entity",
		" • distance",
		"You cannot have an ending entity and ending location, this will error. You will need to choose one.",
		"Some things cannot be caught at parse time, however there are still things to bear in mind.",
		" • Raw input should be treated as they are in command entries, you do not need to wrap them in quotes.",
		" • The duration can be forever, or a timespan.",
		" • Distance will default at 100.",
		" • Type will default to guardian.",
		" • Expressions will be parsed.",
		" • The only laser types are \"crystal [beam]\" and \"guardian\"",
		" • You cannot use an ending entity for a crystal laser."})
@Examples({"create laser keyed as \"aespa solos ur favs\":",
		"\tduration: forever",
		"\ttype: crystal",
		"\tstart location: {-teams::blue::leader}",
		"\tend location: {-teams::blue::leader}'s target block",
		"\tdistance: 240",
		"show laser with id \"aespa solos ur favs\" to {-teams::blue::players}"})
@Since("2.0")
public class SecLaser extends Section {

	static {
		if (Laser.isEnabled()) {
			Skript.registerSection(SecLaser.class,
					"create [a] [new] la(s|z)er (with key|(key|nam)ed [as]) %string%");
		}
	}

	private static Expression<?> parseExpr(String raw, Class<?> type) {
		return new SkriptParser(raw, SkriptParser.ALL_FLAGS, ParseContext.DEFAULT).parseExpression(type);
	}

	private static final SectionValidator SECTION_VALIDATOR = new SectionValidator()
			.addEntry("duration", false)
			.addEntry("start location", false)
			.addEntry("type", true)
			.addEntry("end location", true)
			.addEntry("end entity", true)
			.addEntry("distance", true);

	private Expression<String> keyExpr, typeExpr;
	private Expression<Timespan> durationExpr;
	private Expression<Location> startLocationExpr, endLocationExpr;
	private Expression<LivingEntity> endEntityExpr;
	private Expression<Number> distanceExpr;

	private String rawDuration;
	boolean isEndingEntity;

	@Override
	public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> list) {
		keyExpr = (Expression<String>) exprs[0];
		sectionNode.convertToEntries(0);
		if (SECTION_VALIDATOR.validate(sectionNode)) {
			// Get raw values
			final String rawStartLocation = ScriptLoader.replaceOptions(sectionNode.getValue("start location"));
			final String rawEndLocation = sectionNode.getValue("end location");
			final String rawEndEntity = sectionNode.getValue("end entity");
			if (rawEndLocation != null && rawEndEntity != null) {
				Skript.error("You cannot have an ending location and an ending entity, you need to decide between the two.");
				return false;
			} else if (rawEndLocation == null && rawEndEntity == null) {
				Skript.error("You must have an ending location or ending entity.");
				return false;
			}
			isEndingEntity = rawEndEntity != null;
			final String rawType = sectionNode.getValue("type");
			final String rawDistance = sectionNode.getValue("distance");
			rawDuration = ScriptLoader.replaceOptions(sectionNode.getValue("duration"));

			// Expressions
			durationExpr = (Expression<Timespan>) SecLaser.parseExpr(rawDuration, Timespan.class);
			if (durationExpr == null) {
				Skript.error("No matching timespan for entry 'duration': " + rawDuration);
				return false;
			}
			startLocationExpr = (Expression<Location>) SecLaser.parseExpr(rawStartLocation, Location.class);
			if (startLocationExpr == null) {
				Skript.error("No matching location for entry 'start location': " + rawStartLocation);
				return false;
			}
			if (rawType != null)
				typeExpr = (Expression<String>) SecLaser.parseExpr(ScriptLoader.replaceOptions(rawType), String.class);

			if (rawDistance != null)
				distanceExpr = (Expression<Number>) SecLaser.parseExpr(ScriptLoader.replaceOptions(rawDistance), Number.class);

			if (rawEndEntity != null)
				endEntityExpr = (Expression<LivingEntity>) SecLaser.parseExpr(ScriptLoader.replaceOptions(rawEndEntity), LivingEntity.class);

			if (rawEndLocation != null)
				endLocationExpr = (Expression<Location>) SecLaser.parseExpr(ScriptLoader.replaceOptions(rawEndLocation), Location.class);
			return true;
		}
		return false;
	}

	@Override
	protected @Nullable TriggerItem walk(Event event) {
		// Key
		String key = keyExpr.getSingle(event);
		if (key == null) return walk(event, false);
		if (LaserManager.get().keyInUse(key)) return walk(event, false);

		// Duration
		int duration;
		if (rawDuration.equalsIgnoreCase("forever")) {
			duration = -1;
		} else {
			final Timespan timespan = durationExpr.getSingle(event);
			if (timespan == null) return walk(event, false);
			duration = LaserManager.get().secondsFromTimespan(timespan);
		}

		// Start Location
		final Location startLocation = startLocationExpr.getSingle(event);
		if (startLocation == null) return walk(event, false);

		// Type
		boolean isGuardian = false;
		if (typeExpr == null) isGuardian = true;
		else {
			final String type = typeExpr.getSingle(event);
			if (type == null || type.equalsIgnoreCase("guardian")) isGuardian = true;
			else if (!type.equalsIgnoreCase("crystal") && !type.equalsIgnoreCase("crystal beam"))
				return walk(event, false);
			else if (isEndingEntity) return walk(event, false);
		}

		// Distance
		int distance;
		if (distanceExpr == null) distance = 100;
		else {
			Number distanceNum = distanceExpr.getSingle(event);
			if (distanceNum == null) distance = 100;
			else distance = distanceNum.intValue();
		}

		if (isEndingEntity) {
			// End Entity
			if (endEntityExpr == null) return walk(event, false);
			final LivingEntity endEntity = endEntityExpr.getSingle(event);
			if (endEntity == null) return walk(event, false);

			try {
				final Laser laser = new Laser.GuardianLaser(startLocation, endEntity, duration, distance);
				laser.executeEnd(() -> LaserManager.get().deleteLaser(key));
				LaserManager.get().createLaser(key, laser);
			} catch (ReflectiveOperationException ex) {
				ex.printStackTrace();
			}
		} else {
			if (endLocationExpr == null) return walk(event, false);
			final Location endLocation = endLocationExpr.getSingle(event);
			if (endLocation == null) return walk(event, false);

			try {
				Laser laser;
				if (isGuardian)
					laser = new Laser.GuardianLaser(startLocation, endLocation, duration, distance);
				else
					laser = new Laser.CrystalLaser(startLocation, endLocation, duration, distance);
				laser.executeEnd(() -> LaserManager.get().deleteLaser(key));
				LaserManager.get().createLaser(key, laser);
			} catch (ReflectiveOperationException ex) {
				ex.printStackTrace();
			}

		}
		return walk(event, true);
	}

	@Override
	public String toString(@Nullable Event event, boolean b) {
		return "create new laser keyed as " + keyExpr.toString(event, b);
	}
}