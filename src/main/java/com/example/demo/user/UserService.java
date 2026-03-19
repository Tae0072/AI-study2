package com.example.demo.user;

import com.example.demo.board.BoardRepository;
import com.example.demo.reply.ReplyRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo._core.handler.ex.Exception400;

@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    public UserService(UserRepository userRepository, BoardRepository boardRepository, ReplyRepository replyRepository) {
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
        this.replyRepository = replyRepository;
    }

    @Transactional
    public void join(UserRequest.JoinDTO joinDTO) {
        // 1. 아이디 중복 체크 (안전 장치)
        userRepository.findByUsername(joinDTO.getUsername()).ifPresent(user -> {
            throw new Exception400("중복된 아이디입니다.");
        });

        // 2. 비밀번호 암호화 (BCrypt)
        String encPassword = BCrypt.hashpw(joinDTO.getPassword(), BCrypt.gensalt());
        joinDTO.setPassword(encPassword);

        // 3. 저장
        userRepository.save(joinDTO.toEntity());
    }

    public User login(UserRequest.LoginDTO loginDTO) {
        User user = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new Exception400("아이디가 존재하지 않습니다."));

        // BCrypt 비교
        if (!BCrypt.checkpw(loginDTO.getPassword(), user.getPassword())) {
            throw new Exception400("비밀번호가 틀렸습니다.");
        }

        return user;
    }

    public boolean usernameSameCheck(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Transactional
    public User 회원정보수정(Integer id, UserRequest.UpdateDTO updateDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception400("회원 정보를 찾을 수 없습니다."));

        // 비밀번호 암호화 후 업데이트
        String encPassword = BCrypt.hashpw(updateDTO.getPassword(), BCrypt.gensalt());
        user.setPassword(encPassword);
        user.setEmail(updateDTO.getEmail());
        user.setPostcode(updateDTO.getPostcode());
        user.setAddress(updateDTO.getAddress());
        user.setDetailAddress(updateDTO.getDetailAddress());
        user.setExtraAddress(updateDTO.getExtraAddress());

        return user; // 더티 체킹으로 업데이트됨
    }

    @Transactional
    public void 회원탈퇴(Integer id) {
        // 1. 해당 사용자의 댓글 삭제
        replyRepository.deleteByUserId(id);
        
        // 2. 해당 사용자의 게시글에 달린 댓글들 삭제 (Cascade 대응)
        replyRepository.deleteByBoardUserId(id);

        // 3. 해당 사용자의 게시글 삭제
        boardRepository.deleteByUserId(id);

        // 4. 사용자 삭제
        userRepository.deleteById(id);
    }
}
