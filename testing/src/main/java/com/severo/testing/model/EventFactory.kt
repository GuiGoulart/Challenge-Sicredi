package com.severo.testing.model

import com.severo.core.domain.model.Event

class EventFactory {

    fun create(events: Events) = when (events) {
        Events.ADOCAO -> Event(
            description = "O Patas Dadas estará na Redenção, nesse domingo, com cães para adoção e produtos à venda!\\n\\nNa ocasião, teremos bottons, bloquinhos e camisetas!\\n\\nTraga seu Pet, os amigos e o chima, e venha aproveitar esse dia de sol com a gente e com alguns de nossos peludinhos - que estarão prontinhos para ganhar o ♥ de um humano bem legal pra chamar de seu. \\n\\nAceitaremos todos os tipos de doação:\\n- guias e coleiras em bom estado\\n- ração (as que mais precisamos no momento são sênior e filhote)\\n- roupinhas \\n- cobertas \\n- remédios dentro do prazo de validade",
            image = "http://lproweb.procempa.com.br/pmpa/prefpoa/seda_news/usu_img/Papel%20de%20Parede.png",
            price = 29.99,
            title = "Feira de adoção de animais na Redenção",
            id = 1
        )
        Events.DOACAO -> Event(
            description = "Vamos ajudar !!\\n\\nSe você tem na sua casa roupas que estão em bom estado de uso e não sabemos que fazer, coloque aqui na nossa página sua cidade e sua doação, com certeza poderá ajudar outras pessoas que estão passando por problemas econômicos no momento!!\\n\\nAjudar não faz mal a ninguém!!!\\n",
            image = "http://fm103.com.br/wp-content/uploads/2017/07/campanha-do-agasalho-balneario-camboriu-2016.jpg",
            price = 59.99,
            title = "Doação de roupas",
            id = 2
        )
    }

    sealed class Events {
        object ADOCAO : Events()
        object DOACAO : Events()
    }
}