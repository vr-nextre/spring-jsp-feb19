package it.nextre.academy.springdemo.service.impl;

import it.nextre.academy.springdemo.entity.Pasto;
import it.nextre.academy.springdemo.repository.DealRepository;
import it.nextre.academy.springdemo.repository.PastoRepository;
import it.nextre.academy.springdemo.service.PastoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PastoServiceImpl implements PastoService {

    @Autowired
    DealRepository dealRepository;

    @Autowired
    PastoRepository pastoRepository;

    @Override
    public List<Pasto> getAll() {
        //return dealRepository.getPasti();
        return pastoRepository.findAll();
    }

    @Override
    public boolean salva(Pasto pasto) {
        if (pasto==null)return false;
        if (pasto.getId()==null) {
            //inserisco
            if (pasto.getTitolo() != null && pasto.getTitolo().length() > 0) {
                //return dealRepository.salvaPasto(pasto);
                pasto = pastoRepository.save(pasto);
                return pasto.getId()!=null ? true : false;
            }
        }else{
            //modifica
            if (pasto.getId()>0 && dealRepository.getPasto(pasto.getId())!=null){
                //return dealRepository.aggiornaPasto(pasto);
                pasto = pastoRepository.save(pasto);
                return true;

            }
        }
        return false;
    }


    @Override
    public List<Pasto> cerca(String nome) {
        if (nome != null && nome.trim().length()>0){
            //return dealRepository.findByNome(nome);
            return pastoRepository.findAllByTitolo(nome);
        }
        return null;
    }

    @Override
    public Pasto getOne(Integer id) {
        if (id !=null && id > 0){
            //return dealRepository.getPasto(id);
            return pastoRepository.findById(id).get();
        }
        return null;
    }
}//end class
