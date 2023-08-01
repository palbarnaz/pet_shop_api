create table services (
    id uuid primary key,
    id_admin uuid not null references users(id),
    description text not null,
    duration int,
    price money NOT NULL

);