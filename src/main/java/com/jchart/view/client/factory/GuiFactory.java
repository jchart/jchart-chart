package com.jchart.view.client.factory;

public class GuiFactory {

   private static GuiFactoryIntr _guiFactory = null;

   public static GuiFactoryIntr getInstance() {
      return _guiFactory;
   }

   public static void init(String className) throws Exception {
      try {
         _guiFactory = (GuiFactoryIntr) Class.forName(className).newInstance();
      } catch (Exception e) {
         throw new Exception(
               "Exception while getting GuiFactoryIntr: \n" + e.getMessage());
      }
   }

}
