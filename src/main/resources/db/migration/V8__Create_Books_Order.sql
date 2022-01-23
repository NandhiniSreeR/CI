CREATE TABLE IF NOT EXISTS public.books_order
(
    book_id bigint NOT NULL,
    order_id bigint NOT NULL,
    CONSTRAINT fk92bolhxv3i3imr8omx2xlqlil FOREIGN KEY (order_id)
        REFERENCES public.books (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkg77fhl098601qaymww10hhd2r FOREIGN KEY (book_id)
        REFERENCES public.orders (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)