package shop.mtcoding.blog.dto;

import lombok.Getter;
import lombok.Setter;

/*
 * 글수정 API
 * 1. URL : http://localhost:8080/board/{id}/update
 * 2. method : POST
 * 3. 요청 body : title=값(String)&content=값(String)
 * 4. MIME타입 : x-www-form-urlencoded
 * 5. 응답 : view(html)를 응답함. detail 페이지
 */

@Getter
@Setter
public class UpdateDTO {
    private int id;
    private String title;
    private String content;
}
