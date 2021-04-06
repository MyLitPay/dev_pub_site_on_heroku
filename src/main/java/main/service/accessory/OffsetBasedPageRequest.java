package main.service.accessory;

import main.model.ModerationStatus;
import main.repo.CommentRepository;
import main.service.CommentService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Date;

public class OffsetBasedPageRequest implements Pageable {
    private int limit;
    private int offset;
    private Sort sort = Sort.by("time").descending();

    public OffsetBasedPageRequest(int limit, int offset) {
        this.limit = limit;
        this.offset = offset;
    }

//    public OffsetBasedPageRequest(int limit, int offset, String mode) {
//        this.limit = limit;
//        this.offset = offset;
//        sortByMode(mode);
//    }
//
//    private void sortByMode(String mode) {
//        switch (mode) {
//            case "popular":
//                postList =  postRepository.findPopular(
//                        (byte) 1, ModerationStatus.ACCEPTED.toString(), new Date(), pageable);
//                int count =
//                sort = Sort.by()
//                break;
//            case "best":
//                postList = postRepository.findBest(
//                        (byte) 1, ModerationStatus.ACCEPTED.toString(), new Date(), pageable);
//                break;
//            case "early":
//                postList = postRepository.findEarly(
//                        (byte) 1, ModerationStatus.ACCEPTED.toString(), new Date(), pageable);
//                break;
//            default:
//                postList = postRepository.findByIsActiveAndModerationStatusAndTimeBefore(
//                        (byte) 1, ModerationStatus.ACCEPTED, new Date(), pageable);
//                break;
//        }
//    }

    @Override
    public int getPageNumber() {
        return offset / limit;
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return new OffsetBasedPageRequest(getPageSize(), (int) (getOffset() + getPageSize()));
    }

    public Pageable previous() {
        return hasPrevious() ?
                new OffsetBasedPageRequest(getPageSize(), (int) (getOffset() - getPageSize()))
                : this;
    }

    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? previous() : first();
    }

    @Override
    public Pageable first() {
        return new OffsetBasedPageRequest(getPageSize(), 0);
    }

    @Override
    public boolean hasPrevious() {
        return offset > limit;
    }
}
