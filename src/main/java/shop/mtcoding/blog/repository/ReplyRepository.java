package shop.mtcoding.blog.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.ReplyWriteDTO;
import shop.mtcoding.blog.model.Reply;

// UserController, BoardController, ReplyController, ErrorController
// UserRepository, BoardRepository, ReplyRepository
// EntityManager, HttpSession
@Repository
public class ReplyRepository {

    @Autowired
    private EntityManager em;

    public Reply findById(int id) {
        System.out.println("테스트 : 1");
        Query query = em.createNativeQuery("select * from reply_tb where id = :id", Reply.class);
        System.out.println("테스트 : 2");
        query.setParameter("id", id);
        System.out.println("테스트 : 3");
        return (Reply) query.getResultList();

    }

    public List<Reply> findByBoardId(Integer boardId) {
        Query query = em.createNativeQuery("select * from reply_tb where board_id = :boardId", Reply.class);
        query.setParameter("boardId", boardId);
        return query.getResultList();
    }

    @Transactional
    public void save(ReplyWriteDTO replyWriteDTO, Integer userId) {
        Query query = em
                .createNativeQuery(
                        "insert into reply_tb(comment, board_id, user_id) values(:comment, :boardId, :userId)");

        query.setParameter("comment", replyWriteDTO.getComment());
        query.setParameter("boardId", replyWriteDTO.getBoardId());
        query.setParameter("userId", userId);
        query.executeUpdate();
    }

    @Transactional
    public void deleteById(Integer id) {
        Query query = em
                .createNativeQuery(
                        "delete from reply_tb where id = :id");

        query.setParameter("id", id);
        query.executeUpdate();
    }
}
