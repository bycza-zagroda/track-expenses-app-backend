package pl.byczazagroda.trackexpensesappbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateWalletDto {

  @Size(max = 20)
  @NonNull
  private String name;
}
