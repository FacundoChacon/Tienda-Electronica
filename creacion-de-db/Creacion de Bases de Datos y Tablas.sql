create database if not exists tienda;
use tienda;

create table categoria(
	id bigint primary key auto_increment not null,
    nombre varchar(25) not null
);

create table usuario(
	id bigint primary key auto_increment not null,
    nombre varchar(35) not null,
    email varchar(50) not null,
    password_hash varchar(30) not null,
    rol varchar(10) not null
);

create table producto(
	id bigint primary key auto_increment not null,
    categoria_id bigint not null,
    nombre varchar(50) not null,
    marca varchar(30) not null,
    precio decimal(10,2) not null,
    stock int not null,
    imagen varchar(50) not null,
    foreign key(categoria_id) references categoria (id)
);

create table orden(
	id bigint primary key auto_increment not null,
    usuario_id bigint not null,
    fecha datetime not null,
    estado varchar(20) not null,
    total decimal(10,2) not null,
    foreign key(usuario_id) references usuario (id)
);

create table detalle_orden(
	id bigint primary key auto_increment not null,
    orden_id bigint not null,
    producto_id bigint not null,
    cantidad int not null,
    precio_unitario decimal(10,2) not null,
    foreign key(orden_id) references orden (id),
    foreign key(producto_id) references producto (id)
)
