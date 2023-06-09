select * from proceso.requerimientohistflujo
WHERE idsolicitud = 94
ORDER BY idsolicitud ASC, idsolestado ASC, pernr ASC, idrol ASC LIMIT 100;

select * from proceso.estructurapersona 
where pernr in ('30034226','30046598','30037628','30044220','30050944','30039842');
--where apepat like '%ysuiza%'
select * from proceso.rol

select * from proceso.requerimientopersona WHERE idsolicitud = 94

SELECT * FROM proceso.requerimientoestado
ORDER BY idsolestado ASC

SELECT * FROM proceso.estructurapersona
where pernr like (select pernr_sup from proceso.estructurapersona where pernr = '30037628')
ORDER BY bukrs ASC, werks ASC, btrtl ASC, pernr ASC LIMIT 100


select * from proceso.sociedadsubdivisionresp;



--proceso.fn_update_requerimiento_persona(integer, character, integer, character, integer, character, text, character, character)

/*

select code,mensaje 
from proceso.fn_update_requerimiento_persona(94,null,3,'30050944',2,'30044220','20056125','WILMER','190.236.4.142')

*/
do $$ 
declare
   --counter    integer := 1;
   solicitante varchar(8) := '30037628';
   --last_name  varchar(50) := 'Doe';
   --payment    numeric(11,2) := 20.5;
begin 



   /*raise notice '% % % has been paid % USD', 
       counter, 
	   first_name, 
	   last_name, 
	   payment;*/
end $$;


SELECT * FROM proceso.estructurapersona
where pernr like (select pernr_sup from proceso.estructurapersona where pernr = '30037628')
ORDER BY bukrs ASC, werks ASC, btrtl ASC, pernr ASC LIMIT 100;



SELECT r.namerol,ep.btrtl,ep.orgeh,ep.email
FROM proceso.estructurapersona ep 
left join proceso.rol r on ep.codrol = r.idrol
--join proceso.estructurapersona ep2 on ep.pernr=ep2.pernr and ep2.orgeh = (select orgeh from proceso.estructurapersona where pernr = (select pernr_solicitante from proceso.requerimientopersona where idsolicitud = 94))
where 
ep.btrtl = (	select btrtl 
				  	from proceso.estructurapersona  
				  	where pernr= (select pernr_solicitante from proceso.requerimientopersona where idsolicitud = 94))

--and ep.orgeh = (	select orgeh 
--				  	from proceso.estructurapersona  
--				  	where pernr= (select pernr_solicitante from proceso.requerimientopersona where idsolicitud = 94))

ORDER BY codrol desc

	

select * from proceso.rol

select * from proceso.requerimientoestado


SELECT email from proceso.estructurapersona where pernr = (select pernr_sup from proceso.estructurapersona where pernr='30037628')

