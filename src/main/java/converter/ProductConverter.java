package converter;

import beans.PreorderBean;
import model.Product;

import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 * ProductConverter
 */
@FacesConverter(value = "converter.ProductConverter")
public class ProductConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value != null) try {
            ELContext elContext = FacesContext.getCurrentInstance().getELContext();
            PreorderBean preorderBean = (PreorderBean) elContext.getELResolver().getValue(elContext, null, "preorderBean");
            return preorderBean.getSelectMenu().get(value);
        } catch (Exception exc) {
            throw new ConverterException((new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid product.")));
        }
        else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value == null) {
            return null;
        } else {
            return String.valueOf(((Product) value).getName());
        }
    }
}
