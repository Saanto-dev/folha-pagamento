# Sistema de Gerenciamento de Funcionários e Folha de Pagamento

Aplicação de console em Java 17 para cadastrar funcionários, calcular remunerações
e gerar relatórios da folha de pagamento. Desenvolvida com POO, arquitetura em
camadas e TDD (JUnit 5). Armazenamento em memória: os dados se perdem ao encerrar.

## Funcionalidades
1. Cadastrar funcionários
2. Listar todos
3. Buscar (por código, CPF ou nome, exato ou parcial; a busca por nome ignora maiúsculas e acentos)
4. Remover
5. Calcular remuneração
6. Exibir dados completos
7. Gerar relatório com funcionários e salários (nomes acima de 20 caracteres são truncados na tabela)
8. Calcular o total da folha

## Categorias e regras de remuneração
| Categoria | Remuneração bruta | Tributos (simplificados) | Exemplo (bruta → líquida) |
|---|---|---|---|
| `FuncionarioCLT` | salário-base + adicional de benefícios | 11% INSS + 7,5% IR | 5.200 + 800 = 6.000 → 4.890 |
| `FuncionarioPJ` | valor por projeto × nº de projetos | não se aplica | 2.500 × 3 = 7.500 → 7.500 |
| `FuncionarioComissionado` | salário-base + (vendas × % comissão) | 10% | 2.500 + 5% de 40.000 = 4.500 → 4.050 |

As alíquotas são fictícias e fixas, definidas para fins didáticos.

**Decisões de projeto**
- O PJ não possui salário-base: a superclasse recebe `0.0`, `setSalarioBase` só aceita 0 e a remuneração vem dos projetos.
- O CPF é imutável após a criação (não há `setCpf`), o que impede burlar a unicidade.
- O PJ não implementa `Tributavel`, pois recolhe os próprios impostos.
- O Comissionado guarda o total de vendas, necessário para calcular a comissão.

## Conceitos de POO aplicados
- **Encapsulamento:** atributos `private`, setters e construtores com validação (`Validador`).
- **Herança e abstração:** classe abstrata `Funcionario` com `calcularRemuneracao()` abstrato.
- **Interfaces:** `Identificavel` (todos) e `Tributavel` (CLT e Comissionado).
- **Override:** `calcularRemuneracao()` sobrescrito em cada subclasse.
- **Overload:** `buscarFuncionario(int)`, `buscarFuncionario(String)` e
  `buscarFuncionario(String, boolean)` em `FuncionarioService`.

## Arquitetura em camadas
| Camada | Pacote | Responsabilidade |
|---|---|---|
| Model | `model` | Entidades, interfaces e regras de cálculo |
| Repository | `repository` | Armazenamento em memória (`LinkedHashMap`), recuperação e unicidade de código e CPF |
| Service | `service` | Coordenação das operações, busca, folha e relatório |
| View | `view` | Menu de console e exibição de resultados |
| Exception | `exception` | Exceções personalizadas |

Fluxo: `View → Service → Repository → Model`.

## Tratamento de exceções
Todas derivam de `FolhaException`:

| Exceção | Quando é lançada |
|---|---|
| `DadosInvalidosException` | campo obrigatório vazio, valor negativo, percentual fora de 0–100 |
| `CpfInvalidoException` | CPF com formato errado ou dígitos verificadores incorretos |
| `FuncionarioNaoEncontradoException` | busca ou remoção de funcionário inexistente |
| `FuncionarioJaCadastradoException` | código ou CPF já cadastrado |

O model valida e lança; o repository garante a unicidade de código e CPF; o service trata
a remoção segura (`removerSeExistir`); a view captura cada tipo e mostra mensagem clara.

## Requisitos
- Java 17 ou superior
- Maven 3.8 ou superior

## Como executar
```bash
git clone https://github.com/Saantos-dev/folha-pagamento.git
cd folha-pagamento
mvn compile exec:java
```

## Como executar os testes
```bash
mvn test                              # todos
mvn test -Dtest=FuncionarioCLTTest    # uma classe
```

## Testes (JUnit 5) e TDD
O desenvolvimento seguiu **Red → Green → Refactor**. O histórico evidencia as etapas
pelos prefixos `test(red)`, `feat(green)` e `refactor` (`git log --oneline --reverse`).

| Camada | Classes de teste |
|---|---|
| model | `FuncionarioTest`, `FuncionarioCLTTest`, `FuncionarioPJTest`, `FuncionarioComissionadoTest`, `IdentificavelTest`, `TributavelTest`, `ValidadorTest`, `PolimorfismoTest` |
| repository | `FuncionarioRepositoryTest` |
| service | `FuncionarioServiceTest` |
| view | `FuncionarioViewTest` (entrada e saída simuladas) |
| exception | um teste para cada exceção e `ExcecoesTest` |

Só a classe `Main` não possui teste unitário (apenas inicialização).

## Tecnologias
Java 17, Maven, JUnit 5, Git e GitHub.

## Autores
- Joao Oliveira (GitHub: [Saantos-dev](https://github.com/Saantos-dev))
