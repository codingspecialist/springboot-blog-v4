package shop.mtcoding.blog.dto;

import lombok.Getter;
import lombok.Setter;

/*
 * 글쓰기 API
 * 1. URL : http://localhost:8080/board/save
 * 2. method : POST
 * 3. 요청 body : title=값(String)&content=값(String)
 * 4. MIME타입 : x-www-form-urlencoded
 * 5. 응답 : view(html)를 응답함. index 페이지
 */

@Getter
@Setter
public class WriteDTO {
    private String title;
    private String content;
}
