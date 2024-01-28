package com.busanit.repository;

import com.busanit.entity.Board;
import com.busanit.entity.QBoard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class BoardRepositoryTest {

    @Autowired
    BoardRepository boardRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("게시글 입력 테스트")
    public void createBoardTest() {
        Board board = new Board();
        board.setTitle("게시글 테스트");
        board.setContent("게시글 테스트 내용입니다.");
        board.setWriter("테스터");

        // save - 저장 및 수정 메서드
        Board saveBoard = boardRepository.save(board);
        System.out.println(saveBoard.toString());
    }

    public void createBoardList() {
        for (int i = 1; i <= 10; i++) {
            Board board = new Board();
            board.setTitle("테스트 제목" + i);
            board.setContent("테스트 내용" + i);
            board.setWriter("테스터" + i);

            Board savedBoard = boardRepository.save(board);
        }
    }

    @Test
    @DisplayName("게시글 조회 테스트")
    public void findByTitleTest() {
        this.createBoardList();
        List<Board> boardList = boardRepository.findByTitle("테스트 제목1");
        for (Board board : boardList) {
            System.out.println(board.toString());
        }
    }

    @Test
    @DisplayName("Querydsl 조회 테스트1")
    public void queryDslTest() {
        this.createBoardList();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QBoard qBoard = QBoard.board;
        JPAQuery<Board> query = queryFactory.selectFrom(qBoard)
                .where(qBoard.content.like("%" + "테스트 내용" + "%"))
                .orderBy(qBoard.title.desc());

        /*
            List<T> fetch() - 조회 결과 리스트 반환
            T fetchOne - 조회 대상이 1건인 경우 제네릭으로 지정한 타입 반환
            T fetchFirst - 조회 대상 중 1건만 반환
            Long fetchCount() - 조회 대상 개수 반환
            QueryResult<T> fetchResults() - 조회한 리스트와 전체 개수를 포함한
                                            QueryResults 반환
         */
        List<Board> boardList = query.fetch();

        for (Board board : boardList) {
            System.out.println(board.toString());
        }
    }
}


