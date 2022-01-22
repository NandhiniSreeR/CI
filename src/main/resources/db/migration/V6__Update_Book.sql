alter table books
    add column image_url varchar(255),
    add column small_image_url varchar(255),
    add column books_count numeric,
    add column isbn13 varchar(255),
    add column isbn varchar(255),
    add column original_publication_year varchar(255),
    add column original_title varchar(255),
    add column language_code varchar(255),
    add column average_rating varchar(255),
    add column order_id bigint;