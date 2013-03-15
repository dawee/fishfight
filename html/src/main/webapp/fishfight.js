$(function () {

    $('body').append(
        $('<div></div>')
            .attr('id', 'playn-root')
            .css('width', window.innerWidth - 1)
            .css('height', window.innerHeight - 1)
    );

    $('#play').click(function () {
        $('#dialog').remove();
    });

});
