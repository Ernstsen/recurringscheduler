export const stringToDate = (dateString: string): Date => {
    let [year, month, day] = dateString.split("-")
    return new Date(parseInt(year), parseInt(month) - 1, parseInt(day), 12)
}
