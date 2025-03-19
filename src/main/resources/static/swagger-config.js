window.onload = function() {
    window.ui = SwaggerUIBundle({
        url: "/v3/api-docs",
        dom_id: "#swagger-ui",
        presets: [SwaggerUIBundle.presets.apis, SwaggerUIStandalonePreset],
        layout: "StandaloneLayout",
        deepLinking: true,
        docExpansion: "none", // Collapse all sections by default
        filter: true
    });

    // Ensure custom CSS is applied
    let link = document.createElement("link");
    link.rel = "stylesheet";
    link.href = "/swagger-custom.css";
    document.head.appendChild(link);

    // Change Swagger UI logo
    let logo = document.querySelector('.topbar-wrapper img');
    if (logo) {
        logo.src = '/custom-logo.png';
    }

    // Modify background color for better contrast
    document.body.style.backgroundColor = "#f8f9fa";
    document.querySelector('.topbar').style.backgroundColor = "#2a2a2a";
};
