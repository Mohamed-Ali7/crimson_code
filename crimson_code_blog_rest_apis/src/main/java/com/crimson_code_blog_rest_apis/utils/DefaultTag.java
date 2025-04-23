package com.crimson_code_blog_rest_apis.utils;

public enum DefaultTag {
    // Programming Languages
    JAVA("Java"),
    PYTHON("Python"),
    JAVASCRIPT("JavaScript"),
    TYPESCRIPT("TypeScript"),
    KOTLIN("Kotlin"),
    GO("Go"),
    RUST("Rust"),
    CSHARP("C#"),
    PHP("PHP"),
    CPLUSPLUS("C++"),
    C("C"),
    SWIFT("Swift"),
    DART("Dart"),
    RUBY("Ruby"),
    VALA("Vala"),
    NIM("Nim"),
    PERL("Perl"),
    JULIA("Julia"),

    // Web Frameworks
    SPRING_BOOT("Spring Boot"),
    DJANGO("Django"),
    EXPRESS("Express"),
    LARAVEL("Laravel"),
    ASP_NET("ASP.NET"),
    NEXT_JS("Next.js"),
    NUXT_JS("Nuxt.js"),
    REACT("React"),
    ANGULAR("Angular"),
    VUE_JS("Vue.js"),
    NODE_JS("Node.js"),

    // Mobile
    ANDROID("Android"),
    IOS("iOS"),
    FLUTTER("Flutter"),
    REACT_NATIVE("React Native"),

    // DevOps & Cloud
    DOCKER("Docker"),
    KUBERNETES("Kubernetes"),
    JENKINS("Jenkins"),
    GITHUB_ACTIONS("GitHub Actions"),
    AWS("AWS"),
    AZURE("Azure"),
    GCP("GCP"),

    // Databases
    MYSQL("MySQL"),
    POSTGRESQL("PostgreSQL"),
    MONGODB("MongoDB"),
    REDIS("Redis"),
    FIREBASE("Firebase"),
    SQL_SERVER("SQL Server"),

    // Tools & Concepts
    JWT("JWT"),
    REST_API("REST API"),
    GRAPHQL("GraphQL"),
    AUTHENTICATION("Authentication"),
    UNIT_TESTING("Unit Testing"),
    INTEGRATION_TESTING("Integration Testing"),
    CI_CD("CI/CD"),
    OAUTH2("OAuth2"),
    MVC("MVC"),
    CLEAN_ARCHITECTURE("Clean Architecture"),

    // AI / Data
    TENSORFLOW("TensorFlow"),
    PYTORCH("PyTorch"),
    PANDAS("Pandas"),
    NUMPY("NumPy"),
    SCIKIT_LEARN("Scikit-learn"),
    DATA_VISUALIZATION("Data Visualization");

    private final String displayName;

    DefaultTag(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

