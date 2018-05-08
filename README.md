#MEDICOS

>NOVO MÉDICO

Método: POST
Url:	<dns>/medico/novo
Parâmetros: 
	Consulta Object
	{	
		nome string	-> Nome do médico
		crm string	-> CRM	(único por médico)
		idade int	-> Idade do médico
		
	}	


>TODOS OS MÉDICOS

Método: GET
Url: <dns>/medico/todos	



>FILTRAR MÉDICOS PELO ID

Método: GET
Url: <dns>/medico/pesquisar/{id}	



>EXCLUIR MÉDICO

Método: GET
Url: <dns>/medico/excluir/{id}	





# CONSULTAS

>NOVA CONSULTA:

Método: POST
Url:	<dns>/consulta/nova
Parâmetros: 
	Consulta Object
	{	
		datahora unix timestamp -> Data e hora da consulta
		p{id int}		-> Paciente Object ## Inerente a entidade Paciente
		m{id int}		-> Medico Object ## Inerente a entidade Medico
		e{id int}		-> EspMed Object ## Inerente a entidade EspMed (Especialidades Médicas)
		c{id int}		-> Consultorio Object ## Inerente a entidade Consultorio
	}	



>TODAS CONSULTAS

Método: GET
Url: <dns>/consulta/todas	



>FILTRAR CONSULTAS PELA ID

Método: GET
Url: <dns>/consulta/pesquisar/{id}	



>EXCLUIR CONSULTA

Método: GET
Url: <dns>/consulta/excluir/{id}	



>FILTRAR CONSULTAS PELA DATA OU HORA

Método: GET
Url: <dns>/consulta/excluir/{data(dd/mm/yyyy), hora(hh:mm) ou timestamp(yyyy-mm-dd hh:mm:ss)}	




