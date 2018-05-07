class AddLocationToSurvivor < ActiveRecord::Migration[5.1]
  def change
    add_reference :survivors, :location, foreign_key: true
  end
end
