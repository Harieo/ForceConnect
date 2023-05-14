package uk.co.harieo.forceop.common;

import org.jetbrains.annotations.NotNull;

/**
 * A wrapper to allow a token to be identified within another string.
 *
 * @param token the token to be wrapped
 */
public record TokenWrapper(String token) {

    public static final String WRAPPER_MARKER = "tkn:";

    /**
     * @return the wrapped token
     */
    public String wrap() {
        return WRAPPER_MARKER + "\"" + token + "\"";
    }

    /**
     * Unwraps a token wrapped by {@link #wrap()} into a {@link TokenWrapper}.
     *
     * @param wrappedToken the token which has been correctly wrapped
     * @return the decoded token in the wrapper record
     * @throws IllegalArgumentException if the wrapped token is invalid
     */
    public static TokenWrapper unwrap(@NotNull String wrappedToken) throws IllegalArgumentException {
        if (wrappedToken.contains(WRAPPER_MARKER)) {
            int markerIndex = wrappedToken.indexOf(WRAPPER_MARKER) + WRAPPER_MARKER.length(); // Positions index at 1 character after marker
            if (markerIndex >= wrappedToken.length()) {
                throw new IllegalArgumentException("Invalid wrapping: Content of wrap truncated");
            }

            char[] wrappedTokenCharArray = wrappedToken.toCharArray();
            if (wrappedTokenCharArray[markerIndex] == '"') { // The containing characters for content will be " "
                StringBuilder tokenBuilder = new StringBuilder();
                for (int i = markerIndex + 1; i < wrappedTokenCharArray.length && wrappedTokenCharArray[i] != '"'; i++) {
                    tokenBuilder.append(wrappedTokenCharArray[i]);
                }

                return new TokenWrapper(tokenBuilder.toString());
            } else {
                throw new IllegalArgumentException("Invalid wrapping: Not followed by \"");
            }
        } else {
            throw new IllegalArgumentException("Not wrapped token: Wrapper marker not present");
        }
    }

}
