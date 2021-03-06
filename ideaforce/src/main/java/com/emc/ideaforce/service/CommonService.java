package com.emc.ideaforce.service;

import com.emc.ideaforce.controller.CommentDto;
import com.emc.ideaforce.model.ChallengeDetail;
import com.emc.ideaforce.model.Story;
import com.emc.ideaforce.model.StoryComments;
import com.emc.ideaforce.model.StoryImage;
import com.emc.ideaforce.model.User;
import com.emc.ideaforce.repository.ChallengeDetailRepository;
import com.emc.ideaforce.repository.StoryCommentRepository;
import com.emc.ideaforce.repository.StoryRepository;
import com.emc.ideaforce.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonService {

    private final ChallengeDetailRepository challengeDetailRepository;

    private final StoryRepository storyRepository;

    private final StoryCommentRepository storyCommentRepository;

    /**
     * Returns the global Challenges list
     */
    public List<ChallengeDetail> getChallengeDetailList() {
        return challengeDetailRepository.findAll();
    }

    /**
     * Return the challenge detail
     *
     * @param id unique identifier for the challenge
     */
    public ChallengeDetail getChallengeDetail(String id) {
        return challengeDetailRepository.getOne(id);
    }

    /**
     * Return the challenges taken by the user
     *
     * @param userId unique identifier for the user
     */
    public List<Story> getStories(String userId) {
        return storyRepository.findAll();   // TODO: Get stories by user id
    }

    /**
     * Submit story taken by the user
     *
     * @param story story taken by user
     */
    public void submitStory(Story story) {
        storyRepository.save(story);
    }

    public List<Story> findAllByApprovedIsFalse() {
        return storyRepository.findAllByApprovedIsFalse();
    }

    @PostConstruct
    public void run() {
        for (int i = 1; i <= 10; i++) {
            challengeDetailRepository
                    .save(new ChallengeDetail(i + "", "CSR Challenge " + i, "About Challenge " + i + "..."));
        }

        storyRepository.deleteAll();

        for (int i = 1; i <= 10; i++) {
            Story story = new Story();

            story.setChallengeId(i + " entry");
            story.setUserId("temp_user");
            story.setDescription("This is sample description posted for current story");
            story.setVideo("https://www.youtube.com/watch?v=k3R09aaWUM8&t=2789s");
            storyRepository.save(story);
        }
    }

    public void setStoryApproved(String entryId) {
        Story storyObj = storyRepository.findStoryByIdEquals(entryId);
        storyObj.setApproved(true);
        storyRepository.save(storyObj );
    }

    public Story getStoryById(String storyId) {
        return storyRepository.findStoryByIdEquals(storyId);
    }

    public void saveStoryComment(CommentDto commentModel, User currentUser) {
        StoryComments storyCommentsEntity = new StoryComments();
        storyCommentsEntity.setStoryId(commentModel.getStoryId());
        storyCommentsEntity.setComment(commentModel.getComment());
        storyCommentsEntity.setUser(currentUser);
        storyCommentsEntity.setCreated(new Date(System.currentTimeMillis()));
        storyCommentsEntity.setLastUpdated(new Date(System.currentTimeMillis()));
        storyCommentRepository.save(storyCommentsEntity);
    }

    public List<StoryComments> getAllCommentsForStory(String id) {
        return storyCommentRepository.findAllByStoryIdEqualsOrderByCreatedDesc(id);
    }
}
