load data
CHARACTERSET UTF8
replace into table my_table
fields terminated by ',' optionally enclosed by '"'
trailing nullcols
(
id,
author,
created DATE "YYYY-MM-DD"
)






