package shop.mtcoding.blog.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.mtcoding.blog.dto.BoardDetailDTO;
import shop.mtcoding.blog.dto.BoardDetailDTOV2;
import shop.mtcoding.blog.dto.UpdateDTO;
import shop.mtcoding.blog.dto.WriteDTO;
import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.repository.BoardRepository;
import shop.mtcoding.blog.repository.ReplyRepository;

@Controller
public class BoardController {

    @Autowired
    private HttpSession session;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @ResponseBody
    @GetMapping("/test/count")
    public String testCount() {
        int count = boardRepository.count("2");
        return count + "";
    }

    // http://localhost:8080?num=4
    @GetMapping({ "/", "/board" })
    public String index(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") Integer page,
            HttpServletRequest request) {
        // 1. 유효성 검사 X
        // 2. 인증검사 X
        System.out.println("테스트 : keyword : " + keyword);
        System.out.println("테스트 : keyword length : " + keyword.length());
        System.out.println("테스트 : keyword isEmpty : " + keyword.isEmpty());
        System.out.println("테스트 : keyword isBlank : " + keyword.isBlank());

        List<Board> boardList = null;
        int totalCount = 0;
        request.setAttribute("keyword", keyword); // 공백 or 값있음
        if (keyword.isBlank()) {
            boardList = boardRepository.findAll(page); // page = 1
            totalCount = boardRepository.count();
        } else {
            boardList = boardRepository.findAll(page, keyword); // page = 1
            totalCount = boardRepository.count(keyword);
        }

        // System.out.println("테스트 : totalCount :" + totalCount);
        int totalPage = totalCount / 3; // totalPage = 1
        if (totalCount % 3 > 0) {
            totalPage = totalPage + 1; // totalPage = 2
        }
        boolean last = totalPage - 1 == page;

        // System.out.println("테스트 :" + boardList.size());
        // System.out.println("테스트 :" + boardList.get(0).getTitle());

        request.setAttribute("boardList", boardList);
        request.setAttribute("prevPage", page - 1);
        request.setAttribute("nextPage", page + 1);
        request.setAttribute("first", page == 0 ? true : false);
        request.setAttribute("last", last);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("totalCount", totalCount);

        return "index";
    }

    @PostMapping("/board/{id}/update")
    public String update(@PathVariable Integer id, UpdateDTO updateDTO) {
        // 1. 인증 검사

        // 2. 권한 체크

        // 3. 핵심 로직
        // update board_tb set title = :title, content = :content where id = :id
        boardRepository.update(updateDTO, id);

        return "redirect:/board/" + id;
    }

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable Integer id, HttpServletRequest request) {
        // 1. 인증 검사

        // 2. 권한 체크

        // 3. 핵심 로직
        Board board = boardRepository.findById(id);
        request.setAttribute("board", board);

        return "board/updateForm";
    }

    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable Integer id) { // 1. PathVariable 값 받기
        // 2.인증검사
        // session에 접근해서 sessionUser 키값을 가져오세요
        // null 이면, 로그인페이지로 보내고
        // null 아니면, 3번을 실행하세요.
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm"; // 401
        }

        // 3. 권한검사
        Board board = boardRepository.findById(id);
        if (board.getUser().getId() != sessionUser.getId()) {
            return "redirect:/40x"; // 403 권한없음
        }

        // 4. 모델에 접근해서 삭제
        // boardRepository.deleteById(id); 호출하세요 -> 리턴을 받지 마세요
        // delete from board_tb where id = :id
        boardRepository.deleteById(id);

        return "redirect:/";
    }

    @PostMapping("/board/save")
    public String save(WriteDTO writeDTO) {
        boardRepository.save(writeDTO, 1);
        return "redirect:/";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {

        return "board/saveForm";
    }

    @ResponseBody
    @GetMapping("/v1/board/{id}")
    public List<BoardDetailDTO> detailV1(@PathVariable Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser"); // 세션접근
        List<BoardDetailDTO> dtos = null;
        if (sessionUser == null) {
            dtos = boardRepository.findByIdJoinReply(id, null);
        } else {
            dtos = boardRepository.findByIdJoinReply(id, sessionUser.getId());
        }
        return dtos;
    }

    @ResponseBody
    @GetMapping("/v2/board/{id}")
    public BoardDetailDTOV2 detailV2(@PathVariable Integer id) {
        session.setAttribute("sessionUser", new User());
        User sessionUser = (User) session.getAttribute("sessionUser"); // 세션접근
        List<BoardDetailDTO> dtos = null;
        BoardDetailDTOV2 dtoV2 = null;
        if (sessionUser == null) {
            dtos = boardRepository.findByIdJoinReply(id, null);
            dtoV2 = new BoardDetailDTOV2(dtos, null);
        } else {
            dtos = boardRepository.findByIdJoinReply(id, sessionUser.getId());
            dtoV2 = new BoardDetailDTOV2(dtos, sessionUser.getId());
        }

        return dtoV2;
    }

    // localhost:8080/board/1
    // localhost:8080/board/50
    @GetMapping("/board/{id}")
    public String detail(@PathVariable Integer id, HttpServletRequest request) { // C
        // mock (세션으로 변경하자)
        int sessionUserId = 1;

        List<BoardDetailDTO> dtos = boardRepository.findByIdJoinReply(id, sessionUserId);

        boolean pageOwner = sessionUserId == id;

        request.setAttribute("dtos", dtos);
        request.setAttribute("pageOwner", pageOwner);

        return "board/detail"; // V
    }
}
