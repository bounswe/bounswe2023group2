create table infoFromTwitter(
    messageid numeric(10) primary key,
    spacesid numeric(10),
    talker character varying(128),
    messagetext character varying (1024),
    messagerelevant bool,
    messageverified bool
)