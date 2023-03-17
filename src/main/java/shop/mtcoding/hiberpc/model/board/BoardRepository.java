package shop.mtcoding.hiberpc.model.board;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BoardRepository {
    private final EntityManager em;

    public Board findById(int id) {
        return em.find(Board.class, id);
    }

    public List<Board> findAll() {
        return em.createQuery("select b from Board b", Board.class)
                .getResultList();
    }

    public Board save(Board board) {
        // primary Key가 있으면 merge, 없으면 save
        if (board.getId() == null) {
            em.persist(board);
        } else {// 업데이트 할땐 값만 바꾸고 호출도 안할 예정임. 더티체킹함!!!
            // 만약 업데이트 한다면 영속화 된 것을 들고와야한다 ...
            Board boardPS = em.find(Board.class, board.getId());
            // 하지만 없는 id를 들고와버린다면??? 그런건 제어 해줘야한다. 검증해서.
            if (boardPS != null) {
                em.merge(board);
            }
            System.out.println("잘못된 머지");
        }
        return board;
    }// update는 save와 같이

    public void delete(Board board) {
        em.remove(board);
    }
}
