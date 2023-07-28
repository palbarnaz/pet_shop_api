create table services (
    id uuid primary key,
    id_admin uuid not null references users(id),
    id_schedule uuid not null references schedules(id),
    description text not null,
    duration varchar(50),
    price money NOT NULL

);