// Initial Setup
const canvas = document.querySelector('canvas');
const c = canvas.getContext('2d');

canvas.width = innerWidth;
canvas.height = innerHeight * 0.25;

const colors = [
	'#5CB85B',
	'#D9534F',
];

addEventListener('resize', () => {
	canvas.width = innerWidth;
	canvas.height = innerHeight;
	init();
});


// Utility Functions
function randomIntFromRange(min,max) {
	return Math.floor(Math.random() * (max - min + 1) + min);
}

function randomColor(colors) {
	return colors[Math.floor(Math.random() * colors.length)];
}


// Objects
function Particle(x, y, radius) {
    this.flag = 0;
	this.x = x;
	this.y = y;
	this.radius = radius;
	this.color = colors[1];
	this.radians = Math.random() * Math.PI * 2;
	this.velocity = 0.05;
	this.distanceFromCenter = randomIntFromRange(0, canvas.width / 2);
    this.distanceOriginal = this.distanceFromCenter;
	this.lastPosition = {
		x: x,
		y: y
	};

	this.update = () => {
		const lastPoint = {
			x: this.x,
			y: this.y
		};
		// Move points over time
		this.radians += this.velocity;
        if (this.distanceFromCenter < 0){
            this.flag = 1;
            this.color = colors[0];
        }
        if (this.distanceOriginal < this.distanceFromCenter){
            this.flag = 0;
            this.color = colors[1];
        }
        if (this.flag == 0){
            this.distanceFromCenter -= 1;
        }
        if (this.flag == 1){
            this.distanceFromCenter += 1;
        }
        // Circular Motion
        this.x = this.lastPosition.x + Math.cos(this.radians) * this.distanceFromCenter;
        this.y = this.lastPosition.y + Math.sin(this.radians) * this.distanceFromCenter;
        this.draw(lastPoint);
	};

	this.draw = lastPoint => {
		c.beginPath();
		c.strokeStyle = this.color;
		c.lineWidth = this.radius;
		c.moveTo(lastPoint.x, lastPoint.y);
		c.lineTo(this.x, this.y);
		c.stroke();
		c.closePath();
	};
}


// Implementation
let particles;
function init() {
	particles = []

	for (let i = 0; i < 400; i++) {
		const radius = (Math.random() * 2) + 1;
		particles.push(new Particle(canvas.width / 2, canvas.height / 2, radius));
	}
}

// Animation Loop
function animate() {
	requestAnimationFrame(animate);
	c.fillStyle = 'rgba(0, 0, 0, 0.5)';
	c.fillRect(0, 0, canvas.width, canvas.height);

	particles.forEach(object => {
		object.update();
	});
}

init();
animate();
