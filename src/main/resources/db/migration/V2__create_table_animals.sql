create table animals (
  id UUID NOT NULL,
  name varchar(100) NOT NULL,
  specie varchar(100) NOT NULL,
  user_id uuid NOT NULL references users(id),
  primary key(id)
);