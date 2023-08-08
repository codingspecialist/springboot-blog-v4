package shop.mtcoding.blog.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import shop.mtcoding.blog.dto.ReplyWriteDTO;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.repository.ReplyRepository;

@Controller
public class ReplyController {

    @Autowired
    private HttpSession session;

    @Autowired
    private ReplyRepository replyRepository;

    @PostMapping("/reply/save")
    public String save(ReplyWriteDTO replyWriteDTO) {
        // comment, boardId 유효성 검사
        if (replyWriteDTO.getBoardId() == null) {
            return "redirect:/40x";
        }
        if (replyWriteDTO.getComment() == null || replyWriteDTO.getComment().isEmpty()) {
            return "redirect:/40x";
        }

        // 인증 검사
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        // 댓글 쓰기
        replyRepository.save(replyWriteDTO, sessionUser.getId());

        return "redirect:/board/" + replyWriteDTO.getBoardId();
    }
}
