package shop.mtcoding.blog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyWriteDTO {
    private Integer boardId;
    private String comment;
}
