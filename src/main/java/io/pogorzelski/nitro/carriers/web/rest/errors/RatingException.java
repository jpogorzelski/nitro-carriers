package io.pogorzelski.nitro.carriers.web.rest.errors;

public class RatingException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public RatingException() {
        super(ErrorConstants.ERR_RATING, "rating", "ratingError");
    }

    public RatingException(String message) {
        super(message, "rating", "ratingError");
    }
}
