(function() {
    function Sprite(url, pos, size, speed, frames, dir, once) {
        this.pos = pos;
        this.size = size;
        this.speed = typeof speed === 'number' ? speed : 0;
        this.frames = frames;
        this._index = 0;
        this.url = url;
        this.dir = dir || 'horizontal';
        this.once = once;
        this.canvasPos = [0, 0];
        this.done = !once;
    };

    Sprite.prototype = {
        update: function(dt) {
            this._index += this.speed * dt;
        },

        render: function(ctx) {
            var frame;

            if(this.speed > 0) {
                var max = this.frames.length;
                var idx = Math.floor(this._index);
                frame = this.frames[idx % max];

                if(this.once && idx >= max) {
                    this.done = true;
                }
            }

            var x = this.pos[0];
            var y = this.pos[1];

            if(this.done){
                frame = this.frames[this.frames.length - 1];
            }

            x += frame * this.size[0];

            img2 = new Image();
            img2.src = "img/bomberman_2.gif";

            ctx.drawImage(img2,
                          x, y,
                          this.size[0], this.size[1],
                          this.canvasPos[0], this.canvasPos[1],
                          this.size[0]*2, this.size[1]*2);
        }
    };

    window.Sprite = Sprite;
})();
