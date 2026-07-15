import pandas as pd
import os

# ----- Function definition -----
def export_to_excel():
    # Should have a global flag, and should only export if changes were made (flag is up).
    input_file = "data/saveFile.txt"
    output_file = "data/LibraryExport.xlsx"

    # Check if the save file exists before trying to read it
    # Watch out for old versions! Do I need to start working with timestamps? 
    if not os.path.exists(input_file):
        print(f"Error: {input_file} not found. Run the Java app first!")
        return

    try:
        # We skip the first 2 lines (Library Name and Book Count) to get straight to the book data
        df = pd.read_csv(input_file, sep=';', skiprows=2, names=[ # Should I place in a loop? Or will it keep reading until the EOF
            "Title", "Author", "ISBN", "Pages", "Year Read", "Category"
        ])

        # Export to Excel
        df.to_excel(output_file, index=False)
        print(f"Successfully exported {len(df)} book(s) to {output_file}")

    except Exception as e: # Too broad!
        print(f"An error occurred during export: {e}")

# ----- Entry point -----
if __name__ == "__main__":
    export_to_excel()