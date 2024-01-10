package me.dio.sacola.service.impl;

import lombok.RequiredArgsConstructor;
import me.dio.sacola.enumeration.FormaPagamento;
import me.dio.sacola.models.Item;
import me.dio.sacola.models.Restaurante;
import me.dio.sacola.models.Sacola;
import me.dio.sacola.repository.ItemRepository;
import me.dio.sacola.repository.ProdutoRepository;
import me.dio.sacola.repository.SacolaRepository;
import me.dio.sacola.resource.dto.ItemDto;
import me.dio.sacola.service.SacolaService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class sacolaServiceImpl implements SacolaService {
   private final SacolaRepository sacolaRepository;
   private final ProdutoRepository produtoRepository;
   private final ItemRepository itemRepository;

    @Override
    public Item incluirItemNaSacola(ItemDto itemDto) {
        Sacola sacola = verSacola(itemDto.getIdSacola());

        if(sacola.isFechada()){
            throw new RuntimeException("Esta sacola está fechada!");
        }

        Item itemParaSerInserido = Item.builder()
                .quantidade(itemDto.getQuantidade())
                .sacola(sacola)
                .produto(produtoRepository.findById(itemDto.getProdutoId()).orElseThrow(
                        () -> {
                            throw new RuntimeException("Esse produto não existe!");
                        }
                ))
                .build();

        var itensSacola = sacola.getItens();

        if(itensSacola.isEmpty()){
            itensSacola.add(itemParaSerInserido);
        } else{
            Restaurante restauranteAtual = itensSacola.get(0).getProduto().getRestaurante();
            Restaurante restauranteDoItemParaAdicionar = itemParaSerInserido.getProduto().getRestaurante();

            // VERIFICA SE O RESTAURANTE QUE A PESSOA ESTÁ TENTANDO ADICIONAR O PRODUTO É IGUAL AO RESTAURANTE QUE ELA JÁ ADICIONOU NA SACOLA (REGRA DE NEGÓCIO DO IFOOD - SÓ PODE PEDIR DE UM RESTAURANTE POR SACOLA)
            if(restauranteAtual.equals(restauranteDoItemParaAdicionar)){
                itensSacola.add(itemParaSerInserido);
            } else {
                throw new RuntimeException("Você só pode inserir itens de um restaurante por sacola!");
            }

        }
        sacolaRepository.save(sacola);
        return itemRepository.save(itemParaSerInserido);
    }

    @Override
    public Sacola verSacola(Long id) {
        return sacolaRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Essa sacola não existe!");
                }
        );
    }

    @Override
    public Sacola fecharSacola(Long id, int numeroFormaPagamento) {
       Sacola sacola = verSacola(id);
       if(sacola.getItens().isEmpty()){
           throw new RuntimeException("A sacola está vazia, coloque itens nela!");
        }

        FormaPagamento formaPagamento =
                numeroFormaPagamento == 0 ? FormaPagamento.DINHEIRO : FormaPagamento.MAQUININHA;

       sacola.setFormaPagamento(formaPagamento);
       sacola.setFechada(true);
       return sacolaRepository.save(sacola);
    }
}








