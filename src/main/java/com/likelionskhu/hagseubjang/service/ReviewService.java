package com.likelionskhu.hagseubjang.service;

import com.likelionskhu.hagseubjang.config.auth.LoginUser;
import com.likelionskhu.hagseubjang.config.auth.dto.SessionUser;
import com.likelionskhu.hagseubjang.domain.lecture.Lecture;
import com.likelionskhu.hagseubjang.domain.lecture.LectureRepository;
import com.likelionskhu.hagseubjang.domain.review.Review;
import com.likelionskhu.hagseubjang.domain.review.ReviewRepository;
import com.likelionskhu.hagseubjang.domain.user.User;
import com.likelionskhu.hagseubjang.domain.user.UserRepository;
import com.likelionskhu.hagseubjang.web.dto.ReviewSaveRequestDto;
import com.likelionskhu.hagseubjang.web.dto.ReviewUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;
    private final HttpSession httpSession;

    @Transactional
    public Review findById(int reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 후기가 없습니다. id=" + reviewId));
        return review;
    }

    @Transactional
    public ReviewSaveRequestDto create(int lectureId) {
        ReviewSaveRequestDto requestDto = new ReviewSaveRequestDto();
        requestDto.setLectureId(lectureId);
        return requestDto;
    }

    @Transactional
    public void save(ReviewSaveRequestDto requestDto) {
        Review review = requestDto.toEntity();

        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        User user1 = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. email=" + user.getEmail()));
        review.setUser(user1);

        Lecture lecture = lectureRepository.findById(requestDto.getLectureId())
                .orElseThrow(() -> new IllegalArgumentException("해당 강좌가 없습니다. id=" + requestDto.getLectureId()));
        review.setLecture(lecture);

        reviewRepository.save(review);
    }

    @Transactional
    public void update(int id, ReviewUpdateRequestDto requestDto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 후기가 없습니다. id=" + id));

        review.update(requestDto.getTitle(), requestDto.getContent());
    }

    @Transactional
    public void delete(int id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 후기가 없습니다. id=" + id));

        reviewRepository.delete(review);
    }
}
