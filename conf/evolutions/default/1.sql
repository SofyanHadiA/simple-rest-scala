# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "CARTS" ("CART_ID" INTEGER NOT NULL PRIMARY KEY,"TOTAL" DOUBLE NOT NULL);
create table "CART_ITEMS" ("CART_ID" INTEGER NOT NULL PRIMARY KEY,"ITEM_ID" INTEGER NOT NULL,"QUANTITY" INTEGER NOT NULL);
create table "ITEMS" ("ITEM_ID" INTEGER NOT NULL PRIMARY KEY,"NAME" VARCHAR NOT NULL,"PRICE" DOUBLE NOT NULL,"STOCK" INTEGER NOT NULL);
alter table "CART_ITEMS" add constraint "CART_FK" foreign key("CART_ID") references "CARTS"("CART_ID") on update NO ACTION on delete NO ACTION;
alter table "CART_ITEMS" add constraint "ITEM_FK" foreign key("ITEM_ID") references "ITEMS"("ITEM_ID") on update NO ACTION on delete NO ACTION;

# --- !Downs

alter table "CART_ITEMS" drop constraint "CART_FK";
alter table "CART_ITEMS" drop constraint "ITEM_FK";
drop table "ITEMS";
drop table "CART_ITEMS";
drop table "CARTS";

