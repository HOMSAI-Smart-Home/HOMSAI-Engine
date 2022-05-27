package app.homsai.engine.media.application.converters;


import app.homsai.engine.media.application.http.dtos.MediaCreateCommandDto;
import app.homsai.engine.media.application.http.dtos.MediaQueriesDto;
import app.homsai.engine.media.application.http.dtos.MediaUpdateCommandDto;
import app.homsai.engine.media.domain.models.Media;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by Giacomo Agostini on 02/12/16.
 */
@Service
public class MediaMapperImpl implements MediaMapper {

    @Autowired
    ModelMapper modelMapper;

    @Override
    public MediaQueriesDto convertToDto(Media media) {
        return modelMapper.map(media, MediaQueriesDto.class);
    }

    @Override
    public Collection<MediaQueriesDto> convertToDto(Collection<Media> media) {
        return modelMapper.map(media, new TypeToken<Collection<MediaQueriesDto>>() {}.getType());
    }

    @Override
    public Media convertFromDto(MediaCreateCommandDto mediaCreateCommandDto) {
        return modelMapper.map(mediaCreateCommandDto, Media.class);
    }

    @Override
    public Media convertFromDto(MediaUpdateCommandDto mediaUpdateCommandDto) {
        return modelMapper.map(mediaUpdateCommandDto, Media.class);
    }
}
