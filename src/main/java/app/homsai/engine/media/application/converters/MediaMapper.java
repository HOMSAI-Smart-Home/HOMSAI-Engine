package app.homsai.engine.media.application.converters;

import app.homsai.engine.media.application.http.dtos.MediaCreateCommandDto;
import app.homsai.engine.media.application.http.dtos.MediaQueriesDto;
import app.homsai.engine.media.application.http.dtos.MediaUpdateCommandDto;
import app.homsai.engine.media.domain.models.Media;

import java.util.Collection;

/**
 * Created by Giacomo Agostini on 02/12/16.
 */

public interface MediaMapper {

    MediaQueriesDto convertToDto(Media media);

    Collection<MediaQueriesDto> convertToDto(Collection<Media> media);

    Media convertFromDto(MediaCreateCommandDto mediaCreateCommandDto);

    Media convertFromDto(MediaUpdateCommandDto mediaUpdateCommandDto);

}
