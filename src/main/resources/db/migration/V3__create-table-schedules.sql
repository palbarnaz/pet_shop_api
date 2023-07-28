  create table schedules (
       id uuid primary key,
      "date" date not null,
      "hour" int not null,
      id_client uuid not null references users(id),
      id_animal uuid not null references animals(id)
   );
