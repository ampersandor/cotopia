package com.ampersandor.leettrack.service;

public class LeetCodeGraphQL {
    public static final String USER_PROFILE_QUERY = """
        query getUserProfile($username: String!) {
            allQuestionsCount {
                difficulty
                count
            }
            matchedUser(username: $username) {
                contributions {
                    points
                }
                profile {
                    reputation
                    ranking
                }
                submissionCalendar
                submitStats {
                    acSubmissionNum {
                        difficulty
                        count
                        submissions
                    }
                    totalSubmissionNum {
                        difficulty
                        count
                        submissions
                    }
                }
            }
        }
        """.replace("\n", " ").replace("\r", " ");;
}