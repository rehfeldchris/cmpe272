/**
 * Adds a popover to input fields, if markup exists for that field.
 * It just looks for an element with class .custom-popover-data as a
 * sibling which follows the input element, and uses its contents as a popover.
 */
$(function(){
    function showPopover() {
        $(this).popover("show");
    }
    function hidePopover() {
        $(this).popover("hide");
    }
    function showPopoverIfNotFocused() {
        $("input:not(:focus)", this).popover("show");
    }
    function hidePopoverIfNotFocused() {
        $("input:not(:focus)", this).popover("hide");
    }

    $("input[type='text']").each(function(){
        var $input = $(this);
        var popoverHtml = $input.next(".custom-popover-data").html();
        if (!popoverHtml) {
            return;
        }
        $input.popover({
            html: true,
            trigger: "manual",
            template: '<div class="popover special-class"><div class="arrow"></div><div class="popover-inner"><h3 class="popover-title"></h3><div class="popover-content"><p></p></div></div></div>',
            content: popoverHtml
        }).focus(showPopover).blur(hidePopover);

        $input.closest(".input-group").hover(showPopoverIfNotFocused, hidePopoverIfNotFocused);

        //$el.hover(showPopover, hidePopoverIfNotFocused)

        ;
    });


});