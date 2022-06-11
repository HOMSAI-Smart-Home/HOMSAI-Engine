package app.homsai.engine.entities.domain.exceptions;

import app.homsai.engine.common.domain.exceptions.NotFoundException;
import app.homsai.engine.common.domain.models.ErrorCodes;

/**
 * Created by Giacomo Agostini on 02/12/16.
 */
public class HvacPowerMeterIdNotSet extends NotFoundException {
    private static final int ERROR_CODE = ErrorCodes.HVAC_POWER_METER_NOT_SET;

    public HvacPowerMeterIdNotSet() {
        super(ERROR_CODE, "hvac power meter not set");
    }
}
