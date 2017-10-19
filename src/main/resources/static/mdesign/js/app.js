'use strict';

$(document).ready(function () {
    $('body').on('click', '[data-rmd-action]',function (e){
        e.preventDefault();

        var action = $(this).data('rmd-action');
        var $this = $(this);

        switch(action) {

            /*-------------------------------------------
                Toggle Block
            ---------------------------------------------*/
            case 'block-open':
                var rmdTarget = $(this).data('rmd-target');
                var rmdBackdrop = $(this).data('rmd-backdrop-class') || '';
                $(rmdTarget).addClass('toggled');

                $('body').addClass('block-opened').append('<div data-rmd-action="block-close" data-rmd-target='+rmdTarget+' class="rmd-backdrop rmd-backdrop--dark ' + rmdBackdrop + '" />');
                $('.rmd-backdrop').fadeIn(300);

                break;

            case 'block-close':
                var rmdTarget = $(this).data('rmd-target');

                $(rmdTarget).removeClass('toggled');
                $('body').removeClass('block-opened');

                $('.rmd-backdrop').fadeOut(300);
                setTimeout(function () {
                    $('.rmd-backdrop').remove();
                }, 300);

                break;


            /*-------------------------------------------
                Navigation close
            ---------------------------------------------*/
            case 'navigation-close':
                $('.navigation').removeClass('toggled');
                $('body').removeClass('block-opened');

                $('.rmd-backdrop').fadeOut(300);
                setTimeout(function () {
                    $('.rmd-backdrop').remove();
                }, 300);


            /*-------------------------------------------
                Advanced Search open/close
            ---------------------------------------------*/
            case 'advanced-search-open':
                $(this).closest('.search__body').addClass('toggled');
                if(!$('.h-backdrop')[0]) {
                    $('#header').append('<div data-rmd-action="advanced-search-close" class="rmd-backdrop search-backdrop" />')
                }

                break;

            case 'advanced-search-close':
                var ascParent = $('.search__body');

                $('.search-backdrop').remove();
                ascParent.addClass('closed');

                setTimeout(function () {
                    ascParent.removeClass('toggled closed');
                }, 270);

                break;


            /*-------------------------------------------
                Print
            ---------------------------------------------*/
            case 'print':
                window.print();

                break;


            /*-------------------------------------------
                Inner page Search open/close
            ---------------------------------------------*/
            case 'inner-search-open':
                $('body').addClass('block-opened').append('<div data-rmd-action="inner-search-close" class="rmd-backdrop--dark rmd-backdrop" />')
                $('#inner-search').addClass('toggled');
                $('.rmd-backdrop').fadeIn(300);

                break;

            case 'inner-search-close':
                $('.rmd-backdrop').fadeOut(300);
                $('body').removeClass('block-opened');

                $('#inner-search').removeClass('toggled');
                setTimeout(function () {
                    $('.rmd-backdrop').remove();
                }, 300);

                break;


            /*-------------------------------------------------
                Switch Login when clicking 'login' in header
            ---------------------------------------------------*/
            case 'switch-login':
                $(this).parent().find('.tab-pane').removeClass('active in');
                $('#top-nav-login').addClass('active in');

                break;


            /*-------------------------------------------------
                Scroll to
            ---------------------------------------------------*/
            case 'scroll-to':
                var scrollToTarget = $(this).data('rmd-target');
                var scrollToOffset = $(this).data('rmd-offset') || 0;
                $('html, body').animate({
                    scrollTop: ($(scrollToTarget).offset().top) - scrollToOffset
                }, 500);

                break;


            /*-------------------------------------------------
                Blog comment reply open/close
            ---------------------------------------------------*/
            case 'blog-comment-open':
                var bcoParent = $(this).closest('.list-group__text');
                var bcoContent =    '<form class="blog-comment__reply animated fadeIn">' +
                                        '<textarea placeholder="Write something..." class="textarea-autoheight"></textarea>' +
                                        '<div class="text-center">' +
                                            '<button class="btn btn-xs btn-link" data-rmd-action="blog-comment-close">Post reply</button>' +
                                            '<button class="btn btn-xs btn-link" data-rmd-action="blog-comment-close">Dismiss</button>' +
                                        '</div>' +
                                    '</form>';

                bcoParent.append(bcoContent);
                autosize($('.textarea-autoheight')); //Re-initiate auto height

                break;

            case 'blog-comment-close':
                var bccTarget = $(this).closest('.list-group__text').find('.blog-comment__reply');
                bccTarget.addClass('fadeOut');

                setTimeout(function () {
                    bccTarget.remove();
                },320)
        }
    });
});

$(document).ready(function () {
    /*------------------------------
        Detect mobile device
    -------------------------------*/
    function isMobile() {
        if( /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {
            return true
        }
    }

    if (isMobile()) {
        $('html').addClass('ismobile');
    }


    /*------------------------------
        Tooltips
    -------------------------------*/
    if($('[data-toggle="tooltip"]')[0]) {
        $('[data-toggle="tooltip"]').tooltip();
    }

    /*------------------------------------------
        Property Type select in Main Search
    -------------------------------------------*/
    if($('.search__type')[0]) {
        $('.search__type input[type="radio"]').on('change', function () {
            var value = $(this).val();
            var parent = $(this).closest('.search__type');

            parent.find('a[data-toggle]').text(value);
            parent.removeClass('open');
        });
    }

    /*------------------------------
        Select2 - Custom Selects
    -------------------------------*/
    if($('select.select2')[0]) {
        $('select.select2').select2({
            dropdownAutoWidth: true,
            width: '100%'
        });
    }


    /*------------------------------
        Silk Carousel
    -------------------------------*/
    if($('.header__recommended')[0]) {
        $('.header__recommended .listings-grid').slick({
            speed: 300,
            slidesToShow: 4,
            slidesToScroll: 1,
            dotsClass: 'slick-dots slick-dots-light',
            infinite: true,
            responsive: [
                {
                    breakpoint: 1200,
                    settings: {
                        slidesToShow: 3,
                        slidesToScroll: 3,
                    }
                },
                {
                    breakpoint: 960,
                    settings: {
                        slidesToShow: 2,
                        slidesToScroll: 2
                    }
                },
                {
                    breakpoint: 700,
                    settings: {
                        slidesToShow: 2,
                        slidesToScroll: 2,
                        dots: true,
                        arrows: false
                    }
                },
                {
                    breakpoint: 550,
                    settings: {
                        slidesToShow: 1,
                        slidesToScroll: 1,
                        dots: true,
                        arrows: false
                    }
                }
            ]
        });
    }

    if($('.grid-widget--listings')[0]) {
        $('.grid-widget--listings').slick({
            slidesToShow: 1,
            slidesToScroll: 1,
            mobileFirst: true,
            dots: true,
            arrows: false,
            dotsClass: 'slick-dots slick-dots-light',
            responsive: [
                {
                    breakpoint: 480,
                    settings: "unslick"

                }
            ]
        });
    }


    /*------------------------------
        Stop Propagation
    -------------------------------*/
    $('body').on('click', '.stop-propagate', function (e) {
        e.stopPropagation();
    });


    /*------------------------------
        Prevent Default
    -------------------------------*/
    $('body').on('click', '.prevent-default', function (e) {
        e.preventDefault();
    });


    /*--------------------------------------------
        Text Fields - Blue Bar when focus
    ---------------------------------------------*/
    if($('.form-group--float')[0]) {
        $('.form-group--float').each(function () {
            var p = $(this).find('.form-control').val()

            if(!p.length == 0) {
                $(this).addClass('form-group--active');
            }
        });

        $('body').on('blur', '.form-group--float .form-control', function(){
            var i = $(this).val();
            var p = $(this).parent();

            if (i.length == 0) {
                p.removeClass('form-group--active');
            }
            else {
                p.addClass('form-group--active');
            }
        });
    }


    /*--------------------------------------------
        Light Gallery - Lightbox
    ---------------------------------------------*/
    if($('.light-gallery')[0]) {
        $('.light-gallery').lightGallery();
    }


    /*--------------------------------------------
        Autosize Textarea
    ---------------------------------------------*/
    if($('.textarea-autoheight')[0]) {
        autosize($('.textarea-autoheight'));
    }


    /*--------------------------------------------
        Rate
    ---------------------------------------------*/
    if($('.rmd-rate')[0]) {
        $('.rmd-rate').each(function () {
            var rate = $(this).data('rate-value');
            var readOnly = $(this).data('rate-readonly');

            $(this).rateYo({
                rating: rate,
                fullStar: true,
                starWidth: '18px',
                ratedFill: '#fcd461',
                normalFill: '#eee',
                readOnly: readOnly || 'false'
            });
        });
    }


    /*--------------------------------------------
        Social Sharing
    ---------------------------------------------*/
    if($('.rmd-share')[0]) {
        $('.rmd-share > div').jsSocials({
            shares: [
                {
                    share: "facebook",
                    label: "",
                    logo: "zmdi zmdi-facebook",
                    shareIn: "blank",
                    css: "rmds-item mdc-bg-indigo-400 animated bounceIn"
                },
                {
                    share: "twitter",
                    label: "",
                    logo: "zmdi zmdi-twitter",
                    shareIn: "blank",
                    css: "rmds-item mdc-bg-cyan-500 animated bounceIn"
                },
                {
                    share: "googleplus",
                    label: "",
                    logo: "zmdi zmdi-google",
                    shareIn: "blank",
                    css: "rmds-item mdc-bg-red-400 animated bounceIn"
                },
                {
                    share: "linkedin",
                    label: "",
                    logo: "zmdi zmdi-linkedin",
                    shareIn: "blank",
                    css: "rmds-item mdc-bg-blue-600 animated bounceIn"
                }
            ]
        });
    }


    /*--------------------------------------------
        Action Header Affix
    ---------------------------------------------*/
    if($('.action-header')[0]) {
        $('#header, .action-header').affix({
            offset: {
                top: $('.action-header').offset().top
            }
        });
    }


    /*--------------------------------------------
        Responsive Tab Navigation
    ---------------------------------------------*/
    if($('[data-rmd-breakpoint]')[0]) {
        $('[data-rmd-breakpoint]').each(function () {
            var breakPoint = $(this).data('rmd-breakpoint');
            var target = $(this);
            var activeItem = $(this).find('ul li.active > a').text();
            target.find('.tab-nav__inner').addClass('dropdown').prepend('<a class="tab-nav__toggle" data-toggle="dropdown">'+ activeItem +'</a>');


            $(window).resize(function(){
                if($(this).width() < breakPoint){
                    target.addClass('tab-nav--mobile');
                    target.find('ul').addClass('dropdown-menu');
                }
                else {
                    target.removeClass('tab-nav--mobile');
                    target.find('.tab-nav__inner').removeClass('dropdown');
                    target.find('ul').removeClass('dropdown-menu');
                }
            })
            .resize();
        });
    }


    /*----------------------------------------
        Clamp.Js
        - Used in dashboard/notes.html
    -----------------------------------------*/
    if(!$('html').is('.ie9')) {
        if($('.notes__body')[0]) {
            var clamp;

            $('.notes__body').each(function(index, element) {
                if($(this).prev().is('.notes__title')) {
                    clamp = 4;
                }
                else {
                    clamp = 6;
                }

                $clamp(element, { clamp: clamp });
            });
        }
    }


    /*----------------------------------------
        WYSIWYG Editor
        - Used in dashboard/notes.html
    -----------------------------------------*/
    if(!$('html').is('.ie9')) {
        if($('.note-view__body')[0]) {
            $('.note-view__body').trumbowyg({
                autogrow: true,
                btns: [
                    'btnGrp-semantic',
                    ['formatting'],
                    'btnGrp-justify',
                    'btnGrp-lists',
                    ['removeformat']
                ]
            });
        }
    }


    /*--------------------------------------------
        Header Menu Dropdown
    ---------------------------------------------*/
    if(!isMobile()) {
        if($('.navigation__dropdown')[0]) {
            $('.navigation__dropdown').hover(function () {
                $(this).find('.navigation__drop-menu').fadeIn(250);
            }, function () {
                $(this).find('.navigation__drop-menu').fadeOut(200);

            });
        }
    }


    /*--------------------------------------------
        IE9 Placeholder
    ---------------------------------------------*/
    if($('.ie9')[0]) {
        $('input, textarea').placeholder();
    }
});
