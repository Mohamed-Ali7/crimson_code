package com.crimson_code_blog_rest_apis.utils;

public enum DefaultCategory {
    FRONTEND_DEVELOPMENT(
        "Frontend Development",
        "Covers user interface technologies such as HTML, CSS, JavaScript, and frameworks like React, Vue, and Angular."
    ),
    BACKEND_DEVELOPMENT(
        "Backend Development",
        "Focuses on server-side technologies including APIs, databases, authentication, and frameworks like Spring Boot, Node.js, and Django."
    ),
    DEVOPS_INFRASTRUCTURE(
        "DevOps & Infrastructure",
        "Includes topics on CI/CD, cloud platforms (AWS, Azure, GCP), containerization (Docker), and orchestration (Kubernetes)."
    ),
    DATABASES(
        "Databases",
        "Explores relational and non-relational databases such as MySQL, PostgreSQL, MongoDB, and Redis."
    ),
    MOBILE_DEVELOPMENT(
        "Mobile Development",
        "Covers native and cross-platform mobile development with Android, iOS, Flutter, and React Native."
    ),
    CYBERSECURITY(
        "Cybersecurity",
        "Deals with securing applications and systems using techniques like encryption, authentication, JWT, HTTPS, and OWASP practices."
    ),
    AI_MACHINE_LEARNING(
        "AI & Machine Learning",
        "Introduces machine learning concepts, tools, and frameworks used in AI development and data-driven applications."
    ),
    DATA_SCIENCE_ANALYTICS(
        "Data Science & Analytics",
        "Focuses on extracting insights from data using statistical analysis, visualization tools, and machine learning models."
    ),
    GAME_DEVELOPMENT(
        "Game Development",
        "Covers game design principles, engines like Unity and Unreal, and techniques for building interactive gaming experiences."
    );

    private final String displayName;
    private final String description;

    DefaultCategory(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
