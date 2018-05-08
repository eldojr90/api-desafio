# BANCO DE DADOS

> Configurar os dados de conexão em application.properties e na classe com.desafio.ConDB

> Criar um schema antes de iniciar a aplicação conforme a primeira instrução

> Rodar o seguinte script referente aos dados que não serão criados via sistema: script.sql



# ENTIDADE - MEDICOS

>NOVO MÉDICO

-Método: POST
-Url:	<dns>/medico/novo
-Parâmetros: 
	Consulta Object
		
		nome string - Nome do médico
		crm string	- CRM	(único por médico)
		idade int - Idade do médico
		

>TODOS OS MÉDICOS

-Método: GET

-Url: <dns>/medico/todos	


>FILTRAR MÉDICOS PELO ID

-Método: GET

-Url: <dns>/medico/pesquisar/{id}	


>EXCLUIR MÉDICO

-Método: GET

-Url: <dns>/medico/excluir/{id}	



# CONSULTAS

>NOVA CONSULTA:

-Método: POST

-Url:	<dns>/consulta/nova

-Parâmetros: 

	Consulta Object
	
		datahora unix timestamp - Data e hora da consulta
		p - Paciente Object inerente a entidade Paciente
			id int	- Id do paciente 
		m - Medico Object inerente a entidade Medico
			id int	- Id do médico
		e - EspMed Object inerente a entidade EspMed (Especialidades Médicas)
			id int	- Id da especialidade médica 
		c - Consultorio Object inerente a entidade Consultorio
			id int	- Id do consultório
		

>TODAS CONSULTAS

-Método: GET

-Url: <dns>/consulta/todas	



>FILTRAR CONSULTAS PELA ID

-Método: GET

-Url: <dns>/consulta/pesquisar/{id}	



>EXCLUIR CONSULTA

-Método: GET

-Url: <dns>/consulta/excluir/{id}	



>FILTRAR CONSULTAS PELA DATA OU HORA

-Método: GET

-Url: <dns>/consulta/excluir/{data(dd/mm/yyyy), hora(hh:mm) ou timestamp(yyyy-mm-dd hh:mm:ss)}	

