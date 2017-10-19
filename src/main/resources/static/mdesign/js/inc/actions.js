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
