$(".home-index-slider").slick({
    dots: false,
    fade: true,
    infinite: true,
    autoplay: true,
    arrows: true,
    speed: 600,
    prevArrow: '<i class="icofont-arrow-left bamdik"></i>',
    nextArrow: '<i class="icofont-arrow-right dandik"></i>',
    slidesToShow: 1,
    slidesToScroll: 1,
    responsive: [
        { breakpoint: 1200, settings: { slidesToShow: 1, slidesToScroll: 1 } },
        { breakpoint: 992, settings: { slidesToShow: 1, slidesToScroll: 1 } },
        { breakpoint: 768, settings: { slidesToShow: 1, slidesToScroll: 1 } },
        { breakpoint: 576, settings: { slidesToShow: 1, slidesToScroll: 1, arrows: false } }
    ]
});

$(".home-grid-slider, .home-category-slider").slick({
    dots: true,
    fade: false,
    infinite: true,
    autoplay: true,
    arrows: true,
    speed: 600,
    prevArrow: '<i class="icofont-arrow-left bamdik"></i>',
    nextArrow: '<i class="icofont-arrow-right dandik"></i>',
    slidesToShow: 1,
    slidesToScroll: 1,
    responsive: [
        { breakpoint: 1200, settings: { slidesToShow: 1, slidesToScroll: 1 } },
        { breakpoint: 992, settings: { slidesToShow: 1, slidesToScroll: 1 } },
        { breakpoint: 768, settings: { slidesToShow: 1, slidesToScroll: 1 } },
        { breakpoint: 576, settings: { slidesToShow: 1, slidesToScroll: 1, arrows: false } }
    ]
});

$(".home-classic-slider, .suggest-slider, .new-slider, .category-slider, .brand-slider").slick({
    dots: false,
    infinite: true,
    autoplay: true,
    arrows: true,
    speed: 800,
    prevArrow: '<i class="icofont-arrow-left bamdik"></i>',
    nextArrow: '<i class="icofont-arrow-right dandik"></i>',
    slidesToShow: 5,
    slidesToScroll: 2,
    responsive: [
        { breakpoint: 1200, settings: { slidesToShow: 4, slidesToScroll: 2 } },
        { breakpoint: 992, settings: { slidesToShow: 3, slidesToScroll: 3 } },
        { breakpoint: 768, settings: { slidesToShow: 2, slidesToScroll: 2 } },
        { breakpoint: 576, settings: { slidesToShow: 1, slidesToScroll: 1, variableWidth: true, arrows: false } }
    ]
});

$(".blog-slider, .testimonial-slider, .testi-slider, .team-slider, .isotope-slider").slick({
    dots: false,
    infinite: true,
    autoplay: false,
    arrows: true,
    speed: 800,
    prevArrow: '<i class="icofont-arrow-left bamdik"></i>',
    nextArrow: '<i class="icofont-arrow-right dandik"></i>',
    slidesToShow: 3,
    slidesToScroll: 1,
    responsive: [
        { breakpoint: 1200, settings: { slidesToShow: 3, slidesToScroll: 3 } },
        { breakpoint: 992, settings: { slidesToShow: 2, slidesToScroll: 2 } },
        { breakpoint: 768, settings: { slidesToShow: 1, slidesToScroll: 1 } },
        { breakpoint: 576, settings: { slidesToShow: 1, slidesToScroll: 1, arrows: false } }
    ]
});

$(".preview-slider, .details-preview").slick({
    slidesToShow: 1,
    slidesToScroll: 1,
    arrows: true,
    fade: true,
    asNavFor: ".thumb-slider, .details-thumb",
    prevArrow: '<i class="icofont-arrow-left bamdik"></i>',
    nextArrow: '<i class="icofont-arrow-right dandik"></i>',
    responsive: [
        { breakpoint: 576, settings: { slidesToShow: 1, slidesToScroll: 1, arrows: true } }
    ]
});

$(".thumb-slider, .details-thumb").slick({
    slidesToShow: 3,
    slidesToScroll: 1,
    asNavFor: ".preview-slider, .details-preview",
    dots: false,
    arrows: false,
    centerMode: true,
    focusOnSelect: true,
    responsive: [
        { breakpoint: 992, settings: { slidesToShow: 3, slidesToScroll: 1 } },
        { breakpoint: 768, settings: { slidesToShow: 3, slidesToScroll: 1 } },
        { breakpoint: 576, settings: { slidesToShow: 3, slidesToScroll: 1 } },
        { breakpoint: 400, settings: { slidesToShow: 2, slidesToScroll: 1 } }
    ]
});
