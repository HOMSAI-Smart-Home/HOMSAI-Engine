package app.homsai.engine.pvoptimizer.domain.models;

import app.homsai.engine.common.domain.models.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "hvac_equipments")
public class HVACEquipment extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "season")
    private Integer season;

    @Column(name = "minimum_idle_minutes")
    private Integer minimumIdleMinutes;

    @Column(name = "minimum_execution_minutes")
    private Integer minimumExecutionMinutes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public Integer getMinimumIdleMinutes() {
        return minimumIdleMinutes;
    }

    public void setMinimumIdleMinutes(Integer minimumIdleMinutes) {
        this.minimumIdleMinutes = minimumIdleMinutes;
    }

    public Integer getMinimumExecutionMinutes() {
        return minimumExecutionMinutes;
    }

    public void setMinimumExecutionMinutes(Integer minimumExecutionMinutes) {
        this.minimumExecutionMinutes = minimumExecutionMinutes;
    }
}

