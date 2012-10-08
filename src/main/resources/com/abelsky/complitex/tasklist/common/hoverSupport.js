//
// Template of the show-on-hover jQuery script.
//
// Parameters (in String.format() syntax):
//
// %1$s - element which visibility is controlled
// %2$s - element id to toggle visibility
//

$j = jQuery.noConflict();
$j('#%1$s').css({visibility:'hidden'});

$j('#%2$s').hover(function () {
    $j('#%1$s').css({visibility:'visible', outline: 0});
}, function () {
    $j('#%1$s').css({visibility:'hidden'});
});