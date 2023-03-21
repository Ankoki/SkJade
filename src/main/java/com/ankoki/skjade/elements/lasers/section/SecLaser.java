package com.ankoki.skjade.elements.lasers.section;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
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
import com.ankoki.skjade.utils.SectionValidatorPlus;
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
		" • The duration can be a timespan, use -1 seconds for an infinite laser.",
		" • Distance will default at 100.",
		" • Type will default to guardian.",
		" • Expressions will be parsed.",
		" • The only laser types are \"crystal [beam]\" and \"guardian\"",
		" • You cannot use an ending entity for a crystal laser."})
@Examples({"create laser keyed as \"aespa solos ur favs\":",
		"\tduration: -1 seconds",
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

	private static final SectionValidatorPlus SECTION_VALIDATOR = new SectionValidatorPlus()
			.addEntry("duration", Timespan.class, false)
			.addEntry("start location", Location.class, false)
			.addEntry("type", String.class, true)
			.addEntry("end location", Location.class, true)
			.addEntry("end entity", LivingEntity.class, true)
			.addEntry("distance", Number.class, true);

	private Expression<String> keyExpr, typeExpr;
	private Expression<Timespan> durationExpr;
	private Expression<Location> startLocationExpr, endLocationExpr;
	private Expression<LivingEntity> endEntityExpr;
	private Expression<Number> distanceExpr;


	@Override
	public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> list) {
		keyExpr = (Expression<String>) exprs[0];
		sectionNode.convertToEntries(0);
		if (SECTION_VALIDATOR.validate(sectionNode)) {
			endLocationExpr = (Expression<Location>) SECTION_VALIDATOR.getEntry("end location");
			endEntityExpr = (Expression<LivingEntity>) SECTION_VALIDATOR.getEntry("end entity");
			if (endLocationExpr != null && endEntityExpr != null) {
				Skript.error("You cannot have an ending location and an ending entity, you need to decide between the two.");
				return false;
			} else if (endLocationExpr == null && endEntityExpr == null) {
				Skript.error("You must have an ending location or ending entity.");
				return false;
			}
			durationExpr = (Expression<Timespan>) SECTION_VALIDATOR.getEntry("duration");
			startLocationExpr = (Expression<Location>) SECTION_VALIDATOR.getEntry("start location");
			typeExpr = (Expression<String>) SECTION_VALIDATOR.getEntry("type");
			distanceExpr = (Expression<Number>) SECTION_VALIDATOR.getEntry("distance");
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
		final Timespan timespan = durationExpr.getSingle(event);
		if (timespan == null) return walk(event, false);
		duration = LaserManager.get().secondsFromTimespan(timespan);

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
			else if (endEntityExpr != null) return walk(event, false);
		}

		// Distance
		int distance;
		if (distanceExpr == null) distance = 100;
		else {
			Number distanceNum = distanceExpr.getSingle(event);
			if (distanceNum == null) distance = 100;
			else distance = distanceNum.intValue();
		}

		if (endEntityExpr != null) {
			// End Entity
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
	public String toString(@Nullable Event event, boolean debug) {
		return "create new laser keyed as " + keyExpr.toString(event, debug);
	}

}