
package ca.qc.johnabbott.cs616.server.controller;

import ca.qc.johnabbott.cs616.server.model.Picture;
import ca.qc.johnabbott.cs616.server.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.config.Projection;

import java.util.Date;

@RepositoryRestResource(path = "picture")
public interface PictureRepository extends CrudRepository<Picture, Long> {

    @Projection(name = "noPicture", types = {Picture.class})
    public interface NoPicture {
        Long getId();
        User getUser();
        Date getUploadTime();
    }

    @Projection(name = "picture", types = {Picture.class})
    public interface WithPicture{
        Long getId();
        User getUser();
        Date getUploadTime();
        String getPicture();
    }
}

