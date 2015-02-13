package com.algaworks.pedidovenda.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.algaworks.pedidovenda.model.Grupo;
import com.algaworks.pedidovenda.repository.GruposRepository;
import com.algaworks.pedidovenda.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Grupo.class)
public class GrupoConverter implements Converter {
	
	private GruposRepository gruposRepository;
	private Grupo grupo;
	
	public GrupoConverter() {
		gruposRepository = CDIServiceLocator.getBean(GruposRepository.class);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		Grupo retorno = null;
		if (value != null) {
			String nome = new String(value);
			retorno = gruposRepository.porNome(nome);
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		
			if(value != null) {
				Grupo grupo = (Grupo) value;
				return grupo.getNome() != null ? grupo.getNome().toString() : null;
			}
			
		return "";
	}

}
